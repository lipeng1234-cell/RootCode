package com.example.agentdemo.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

public class Agent {

    public void premain(String agentParam, Instrumentation instrumentation) {
        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
                return builder.method(ElementMatchers.<MethodDescription>any()).intercept(MethodDelegation.to(MyInterceptor.class));
            }
        };

        new AgentBuilder.Default().type(ElementMatchers.<TypeDescription>nameStartsWith("")).transform(transformer).installOn(instrumentation);
    }

}
