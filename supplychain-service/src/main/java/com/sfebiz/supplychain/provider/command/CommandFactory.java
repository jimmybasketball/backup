package com.sfebiz.supplychain.provider.command;

import com.sfebiz.supplychain.exposed.common.code.SCReturnCode;
import com.sfebiz.supplychain.provider.command.common.CommandConfig;
import net.pocrd.entity.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 命令工厂类
 */
public class CommandFactory {

    private static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);

    /**
     * 创建仓库命令
     *
     * @param version
     * @param key
     * @return
     * @throws ServiceException
     */
    public static ProviderCommand createCommand(String version, String key) throws ServiceException {
        if (CommandConfig.getInstance().getLpcMapping() == null ||
                !CommandConfig.getInstance().getLpcMapping().containsKey(version) ||
                !CommandConfig.getInstance().getLpcMapping().get(version).containsKey(key)) {
            throw new ServiceException(SCReturnCode.VERSION_ERR, SCReturnCode.VERSION_ERR.getDesc() + "version:" + version + ",key:" + key);
        }

        Map<String, String> params = CommandConfig.getInstance().getLpcMapping().get(version).get(key);
        if (params == null || !params.containsKey("class_name")) {
            throw new ServiceException(SCReturnCode.KEY_ERROR, SCReturnCode.KEY_ERROR.getDesc() + "version:" + version + ",key:" + key);
        }

        String cmd = params.get("class_name");

        try {
            @SuppressWarnings("unchecked")
            Class<ProviderCommand> cls = (Class<ProviderCommand>) Class.forName(cmd);
            ProviderCommand command = cls.newInstance();
            ((AbstractCommand) command).setParams(params);
            return command;
        } catch (ClassNotFoundException e) {
            throw new ServiceException(SCReturnCode.COMMAND_INNER_EXCEPTION, SCReturnCode.COMMAND_INNER_EXCEPTION.getDesc() + "version:" + version + ",key:" + key);
        } catch (InstantiationException e) {
            throw new ServiceException(SCReturnCode.COMMAND_INNER_EXCEPTION, SCReturnCode.COMMAND_INNER_EXCEPTION.getDesc() + "version:" + version + ",key:" + key);
        } catch (IllegalAccessException e) {
            throw new ServiceException(SCReturnCode.COMMAND_INNER_EXCEPTION, SCReturnCode.COMMAND_INNER_EXCEPTION.getDesc() + "version:" + version + ",key:" + key);
        }
    }


    /**
     * 如果支持口岸，创建口岸命令
     *
     * @param version
     * @param key
     * @return
     * @throws ServiceException
     */
    public static ProviderCommand createPortCommand(String version, String key)
            throws ServiceException {
        if (CommandConfig.getInstance().getPortMapping() == null || !CommandConfig.getInstance().getPortMapping().containsKey(version) ||
                !CommandConfig.getInstance().getPortMapping().get(version).containsKey(key)) {
            throw new ServiceException(SCReturnCode.VERSION_ERR, SCReturnCode.VERSION_ERR.getDesc() + "version:" + version + ",key:" + key);
        }

        Map<String, String> params = CommandConfig.getInstance().getPortMapping()
                .get(version).get(key);
        if (!params.containsKey("class_name")) {
            throw new ServiceException(SCReturnCode.VERSION_ERR, SCReturnCode.VERSION_ERR.getDesc() + "version:" + version + ",key:" + key);
        }

        String cmd = params.get("class_name");

        try {
            @SuppressWarnings("unchecked")
            Class<ProviderCommand> cls = (Class<ProviderCommand>) Class.forName(cmd);
            ProviderCommand c = cls.newInstance();
            ((AbstractCommand) c).setParams(params);
            return c;
        } catch (ClassNotFoundException e) {
            throw new ServiceException(SCReturnCode.COMMAND_INNER_EXCEPTION, SCReturnCode.COMMAND_INNER_EXCEPTION.getDesc() + "version:" + version + ",key:" + key);
        } catch (InstantiationException e) {
            throw new ServiceException(SCReturnCode.COMMAND_INNER_EXCEPTION, SCReturnCode.COMMAND_INNER_EXCEPTION.getDesc() + "version:" + version + ",key:" + key);
        } catch (IllegalAccessException e) {
            throw new ServiceException(SCReturnCode.COMMAND_INNER_EXCEPTION, SCReturnCode.COMMAND_INNER_EXCEPTION.getDesc() + "version:" + version + ",key:" + key);
        }
    }

}
