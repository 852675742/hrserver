package org.sang.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Rick on 2019/2/26.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString(callSuper = true)
public class HrvError implements Serializable{

    private String code;

    private String msg;


}
