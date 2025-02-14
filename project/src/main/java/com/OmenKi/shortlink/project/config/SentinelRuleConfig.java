package com.OmenKi.shortlink.project.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/2/14
 * @Description: 初始化限流的配置Sentinel
 */
@Component
public class SentinelRuleConfig implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();

        // 设置资源名，应与@SentinelResource中的value对应
        flowRule.setResource("create_short-link");

        // 设置限流指标为QPS
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);

        // 设置阈值，这里是每秒允许1个请求
        flowRule.setCount(1);

        rules.add(flowRule);
        FlowRuleManager.loadRules(rules);
    }
}
