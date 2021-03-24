package com.mycompany.wso2.carbon.identity.jit.provisioning.handler.internal;

import com.mycompany.wso2.carbon.identity.jit.provisioning.handler.CustomJITProvisioningHandler;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(name = "com.mycompany.wso2.carbon.identity.jit.provisioning.handler.internal.CustomJITProvisioningHandlerOSGIBundleManager",
        immediate = true)
public class CustomJITProvisioningHandlerOSGIBundleManager {

    @Activate
    protected void activate(BundleContext bundleContext) {

        bundleContext.registerService(CustomJITProvisioningHandler.class, CustomJITProvisioningHandler.getInstance(), null);
    }
}