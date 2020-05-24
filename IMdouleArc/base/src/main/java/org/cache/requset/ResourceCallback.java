package org.cache.requset;

import org.cache.element.IElement;
import org.cache.exception.KeeperException;
import org.cache.targe.DataSource;

/**
 * Author: yuzzha
 * Date: 12/27/2019 6:27 PM
 * Description:
 * Remark:
 */
public interface ResourceCallback {

    /**
     * Called when a resource is successfully loaded.
     *
     * @param resource The loaded resource.
     */
    void onResourceReady(IElement<?> resource, DataSource dataSource);

    /**
     * Called when a resource fails to load successfully.
     *
     * @param e a non-null {@link KeeperException}.
     */
    void onLoadFailed(KeeperException e);

    /**
     * Returns the lock to use when notifying individual requests.
     */
    Object getLock();
}
