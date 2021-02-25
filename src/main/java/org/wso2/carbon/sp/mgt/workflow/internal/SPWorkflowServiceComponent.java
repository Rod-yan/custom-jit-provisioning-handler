package org.wso2.carbon.sp.mgt.workflow.internal;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.identity.application.mgt.listener.ApplicationMgtListener;
import org.wso2.carbon.identity.workflow.mgt.extension.WorkflowRequestHandler;
import org.wso2.carbon.sp.mgt.workflow.impl.SPCreateHandler;
import org.wso2.carbon.sp.mgt.workflow.impl.SPWorkflowListener;


@Component(
        name = "tenant.mgt.workflow",
        immediate = true

)
public class SPWorkflowServiceComponent {

    @Activate
    protected void activate(ComponentContext context) {

        BundleContext bundleContext = context.getBundleContext();
        bundleContext.registerService(ApplicationMgtListener.class.getName(), new SPWorkflowListener(), null);
        bundleContext.registerService(WorkflowRequestHandler.class.getName(), new SPCreateHandler(), null);
    }
}