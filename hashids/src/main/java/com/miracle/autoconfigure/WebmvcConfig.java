package com.miracle.autoconfigure;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.miracle.annotation.Hashids;
import com.miracle.formatter.AnnotationUtils;
import com.miracle.formatter.DateConverter;
import com.miracle.formatter.HashidsFormatterFactory;
import com.miracle.formatter.HashidsUtils;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Rick on 2019/3/12.
 *
 */
@Configuration
public class WebmvcConfig extends WebMvcConfigurationSupport{


    /**
     * 添加普通输入参数的hashid转换，如@RequestParam, @PathVariable注解的参数
     *
     * @param registry
     */
    protected void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addConverter(new DateConverter());
        HashidsFormatterFactory hashidsFormatterFactory = new HashidsFormatterFactory();
        registry.addFormatterForFieldAnnotation(hashidsFormatterFactory);
    }

    /**
     * 添加json输入和输出的hashid转换器，并且依赖使用FastJson，如@RequestBody注解的参数和返回json格式的数据
     *
     * @param converters
     */
    /*
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter convert = fastJsonHttpMessageConverters();
        converters.add(convert);
    }
    */

    /**
     * 配置fastjson转换器
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        System.out.println("进来json转换器");
        // 1.定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter(){
            public Object read (Type type, Class<?> contextClass, HttpInputMessage inputMessage) {
                try {
                    InputStream is = inputMessage.getBody();
                    Charset charset = getFastJsonConfig().getCharset();
                    String json = IOUtils.toString(is, Objects.isNull(charset)? com.alibaba.fastjson.util.IOUtils.UTF8.name() : charset.name());
                    return JSON.parseObject(json, type,getFastJsonConfig().getParserConfig(),  JSON.DEFAULT_PARSER_FEATURE, getFastJsonConfig().getFeatures());
                } catch (JSONException var4) {
                    throw new HttpMessageNotReadableException("JSON parse error: " + var4.getMessage(), var4);
                } catch (IOException var5) {
                    throw new HttpMessageNotReadableException("I/O error while reading input message", var5);
                }
            }
        };
        // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializeFilters(new ValueFilter() {
            public Object process(Object o, String s, Object o1) {
                if(AnnotationUtils.fieldHasAnnotation(o.getClass(),s, Hashids.class)) {
                    return HashidsUtils.instance.encode((Integer) o1);
                }
                return o1;
            }
        });

        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect);
        //日期格式化
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

        ParserConfig parserConfig = new ParserConfig();
        IntegerCodec integerCodec = new IntegerCodec() {
            @Override
            public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
                // 获取需要转换的类的实例
                Object contentObject = parser.getContext().object;
                System.out.println("xx1:" + contentObject.getClass().getName());
                System.out.println("xx2:" + fieldName);
                // 判断字段属性是否包含HashidsFormat注解
                if (contentObject != null && AnnotationUtils.fieldHasAnnotation(contentObject.getClass(), ((String) fieldName), Hashids.class)) {
                    JSONLexer lexer = parser.lexer;
                    int token = lexer.token();
                    System.out.println("token:" + token);
                    if (token == JSONToken.NULL) {
                        lexer.nextToken(JSONToken.COMMA);
                        return null;
                    }
                    // 字符串Id转换为整型
                    if (token == JSONToken.LITERAL_STRING) {
                        String value = (String) parser.parse();
                        if (!value.matches("\\d+")) {
                            Integer intObj = HashidsUtils.instance.decode(value);
                            return (T) intObj;
                        }
                    }
                }
                return super.deserialze(parser, clazz, fieldName);
            }
        };

        parserConfig.putDeserializer(int.class,integerCodec);
        parserConfig.putDeserializer(Integer.class,integerCodec);
        fastJsonConfig.setParserConfig(parserConfig);

        // 3.在converter中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);

        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        // 5.返回HttpMessageConverter对象
        HttpMessageConverter<?> converter = fastConverter;
        return new HttpMessageConverters(converter);
    }

}
