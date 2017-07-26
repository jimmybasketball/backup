package com.sfebiz.supplychain.aop.aspect;

import com.sfebiz.common.utils.log.LogBetter;
import com.sfebiz.common.utils.log.LogLevel;
import com.sfebiz.supplychain.aop.annotation.ParamNotBlank;
import com.sfebiz.supplychain.aop.valid.ValidMethodConfig;
import com.sfebiz.supplychain.aop.valid.ValidParamConfig;
import com.sfebiz.supplychain.aop.valid.ValidResult;
import com.sfebiz.supplychain.aop.valid.ValidateFailException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 参数校验横切逻辑
 *
 * @author liujc
 * @create 2017-06-30 18:18
 **/
@Aspect
public class ParamValidAspect {

    private static Logger logger = LoggerFactory.getLogger(ParamValidAspect.class);

    /**
     * 关注的验证注解类型
     */
    private static List<Class<? extends Annotation>> annotationPool = new ArrayList<Class<? extends Annotation>>() {
        {
            this.add(ParamNotBlank.class);
        }
    };

    /**
     * 方法对应方法验证配置缓存
     */
    private static ConcurrentMap<Method, ValidMethodConfig> validMethodConfigCache = new ConcurrentHashMap<Method, ValidMethodConfig>();

    /**
     * 横切关注点
     */
    @Pointcut("@annotation(com.sfebiz.supplychain.aop.annotation.MethodParamValidate)")
    public void paramValidPointcut() {
    }

    /**
     * 横切校验逻辑
     *
     * @param joinPoint
     * @return
     */
    @Around("paramValidPointcut()")
    public Object paramValidate(ProceedingJoinPoint joinPoint) throws Throwable {
        Method currentMethod = null;
        Object target = null;
        try {
            //获取方法签名
            Signature sig = joinPoint.getSignature();
            MethodSignature msig = null;
            msig = (MethodSignature) sig;
            //反射获取目标对象的具体方法实例
            target = joinPoint.getTarget();
            currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            //方法参数值
            Object[] args = joinPoint.getArgs();


            //从缓存中获取方法验证配置
            ValidMethodConfig validMethodConfig = validMethodConfigCache.get(currentMethod);
            if (validMethodConfig == null) {
                //方法上所有参数的所有注解
                Annotation[][] allParameterAnnotations = currentMethod.getParameterAnnotations();
                //方法上参数的校验配置（验证规则和验证值）
                List<ValidParamConfig> validParamConfigs = new ArrayList<ValidParamConfig>();
                //只有验证规则
                List<ValidParamConfig> validParamConfigWithOutValues = new ArrayList<ValidParamConfig>();
                for (int i = 0; i < args.length; i++) {
                    Object paramValue = args[i];
                    Annotation[] parameterAnnotations = allParameterAnnotations[i];
                    //只需要ParamValidAspect.annotationPool中 包含的注解
                    List<Annotation> validAnnotations = new ArrayList<Annotation>();
                    for (Annotation annotation : parameterAnnotations) {
                        if (annotationPool.contains(annotation.annotationType())) {
                            validAnnotations.add(annotation);
                        }
                    }

                    //参数值和参数上对应的验证注解
                    ValidParamConfig validParamConfig = new ValidParamConfig(paramValue, validAnnotations);
                    validParamConfigs.add(validParamConfig);

                    //只存验证注解
                    ValidParamConfig validParamConfigWithOutValue = new ValidParamConfig(null, validAnnotations);
                    validParamConfigWithOutValues.add(validParamConfigWithOutValue);
                }
                validMethodConfig = new ValidMethodConfig(validParamConfigs, validParamConfigWithOutValues, msig.getReturnType());
                validMethodConfigCache.putIfAbsent(currentMethod, validMethodConfig);
            } else {
                //刷新参数校验配置的值
                validMethodConfig.flushParamConfigValue(args);
            }


            LogBetter logBetter = LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[方法参数校验]  开始")
                    .addParm("类", target.getClass().toString())
                    .addParm("方法", currentMethod.getName());
            //获取参数名
            String[] paramNames = msig.getParameterNames();
            for (int i = 0; i < paramNames.length; i++) {
                logBetter.addParm(paramNames[i], args[i]);
            }
            logBetter.log();


            //执行验证
            ValidResult validResult = validMethodConfig.doValidate();
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[方法参数校验]  结束")
                    .addParm("类", target.getClass().toString())
                    .addParm("方法", currentMethod.getName())
                    .addParm("是否校验成功", validResult.isValid() ? "是" : "否")
                    .addParm("校验结果", validResult.getResult())
                    .log();
            if (!validResult.isValid()) {
                //返回验证失败信息
                return validResult.getResult();
            }

        } catch (ValidateFailException e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[方法参数校验]  结束")
                    .addParm("类", target.getClass().toString())
                    .addParm("方法", currentMethod.getName())
                    .addParm("是否校验成功", "否")
                    .addParm("校验结果", e.getViolation())
                    .log();
            throw e;
        } catch (Exception e) {
            LogBetter.instance(logger)
                    .setLevel(LogLevel.INFO)
                    .setMsg("[方法参数校验]  异常")
                    .setException(e)
                    .log();
        }
        return joinPoint.proceed();
    }
}
