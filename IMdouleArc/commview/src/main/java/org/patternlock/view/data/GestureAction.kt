package org.patternlock.view.data

import kotlin.math.sqrt


/**
 * File: GestureAction.java
 * Author: yuzhuzhang
 * Create: 2020/2/27 1:26 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/2/27 : Create GestureAction.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
data class GestureAction(val id :Int,val x:Float,val y :Float,val radius:Float,var isHit:Boolean=false) {

    fun onTouch(x: Float,y: Float,enableSkip: Boolean):Boolean{
        val dx=this.x-x
        val dy=this.y-y
        val dRadous=if(enableSkip){this.radius}else{this.radius*1.5f}
    return sqrt((dx*dx+dy*dy).toDouble()) <=dRadous.toDouble();
    }
}