package org.cache.element;

import androidx.annotation.NonNull;

/**
 * Author: yuzzha
 * Date: 12/27/2019 5:56 PM
 * Description:
 * Remark:
 */
public interface IElement<E> {

    @NonNull
    Class<E> getResourceClass();

    @NonNull
    E get();

    int getSize();

    void recycle();
}
