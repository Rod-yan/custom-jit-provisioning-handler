package org.wso2.carbon.sp.mgt.workflow.impl;

import org.wso2.carbon.identity.application.mgt.listener.AbstractApplicationMgtListener;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;

public class SPWorkflowListener extends AbstractApplicationMgtListener {

    @Override
    public int getDefaultOrderId() {
        return 1;
    }

    @Override
    public boolean doPreCreateApplication(ServiceProvider serviceProvider, String tenantDomain, String userName)
            throws IdentityApplicationManagementException {
        if (!isEnable()) {
            return true;
        }
        try {
            SPCreateHandler spCreateHandler = new SPCreateHandler();
            return spCreateHandler.startSPCreateWorkflow(serviceProvider, tenantDomain, userName);
        } catch (Exception e) {
            // Sending e.getMessage() since it is required to give error message to end user.
            throw new IdentityApplicationManagementException(e.getMessage(), e);
        } finally {
        }
    }

}
