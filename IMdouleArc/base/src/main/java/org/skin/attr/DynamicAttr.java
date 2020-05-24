package org.skin.attr;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author: yuzzha
 * Date: 3/24/2020 2:40 PM
 * Description:
 * Remark:
 *
 * @author yuzzha
 */
@Data
@AllArgsConstructor
public class DynamicAttr {

    /**
     * attr name , defined from {@link AttrFactory} :<br>
     * should be
     * <li> AttrFactory.BACKGROUND
     * <li> AttrFactory.TEXT_COLOR <br>
     * ...and so on
     */
    public String attrName;

    /**
     * resource id from default context , eg: "R.drawable.app_bg"
     */
    public int refResId;
}
