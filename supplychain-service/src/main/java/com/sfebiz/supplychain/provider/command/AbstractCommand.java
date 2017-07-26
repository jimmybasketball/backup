package com.sfebiz.supplychain.provider.command;

import net.pocrd.entity.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 与物流供应商（仓库、物流提供商、口岸、支付）交互的所有命令
 */
public abstract class AbstractCommand implements ProviderCommand {

    protected static final Logger logger = LoggerFactory.getLogger("CommandLogger");

    private Map<String, String> params;

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 同步调用命令链执行
     *
     * @return
     */
    public abstract boolean execute() throws ServiceException;

    /**
     * 异步调命令链执行
     */
    @Override
    public void run() {
        try {
            execute();
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
