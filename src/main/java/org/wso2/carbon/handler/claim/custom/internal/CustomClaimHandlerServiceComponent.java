package org.wso2.carbon.handler.claim.custom.internal;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.handler.claim.custom.CustomClaimHandler;

@Component(name = "org.wso2.carbon.custom.claimhandler.northstar.internal.CustomClaimHandlerServiceComponent",
        immediate = true)
public class CustomClaimHandlerServiceComponent {

    @Activate
    protected void activate(BundleContext bundleContext) {

        bundleContext.registerService(CustomClaimHandler.class, new CustomClaimHandler(), null);
    }
}