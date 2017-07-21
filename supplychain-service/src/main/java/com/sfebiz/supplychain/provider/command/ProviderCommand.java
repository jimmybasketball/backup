package com.sfebiz.supplychain.provider.command;

import net.pocrd.entity.ServiceException;

/**
 * 命令
 */
public interface ProviderCommand extends Runnable {

    /**
     * 调用当前命令，如果失败，则抛异常，只有成功了才返回true
     *
     * @return
     */
    public boolean execute() throws ServiceException;

}
