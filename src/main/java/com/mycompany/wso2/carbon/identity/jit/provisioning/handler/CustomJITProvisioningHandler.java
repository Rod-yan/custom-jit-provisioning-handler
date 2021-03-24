package com.mycompany.wso2.carbon.identity.jit.provisioning.handler;

import com.mycompany.wso2.carbon.identity.jit.provisioning.handler.util.GeneralUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.exception.FrameworkException;
import org.wso2.carbon.identity.application.authentication.framework.handler.provisioning.impl.DefaultProvisioningHandler;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.utils.xml.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomJITProvisioningHandler extends DefaultProvisioningHandler {

    private static final Log log = LogFactory.getLog(CustomJITProvisioningHandler.class);

    private static final String EMAIL_DOMAIN = "@mycompany.com";
    private static final String DEFAULT_USER_ROLE_NAME = "Default User Role";

    private static volatile CustomJITProvisioningHandler instance;

    private CustomJITProvisioningHandler() { }

    public static CustomJITProvisioningHandler getInstance() {
        if (instance == null) {
            synchronized (CustomJITProvisioningHandler.class) {
                if (instance == null) {
                    instance = new CustomJITProvisioningHandler();
                }
            }
        }
        return instance;
    }

    @Override
    public void handle(List<String> roles, String subject, Map<String, String> attributes, String provisioningUserStoreId, String tenantDomain) throws FrameworkException {
        try {

            // Obtains an instance of the User Store Manager.
            // This will come handy for performing validations against the local IdP User Store.
            UserStoreManager userStoreMgr = GeneralUtils.getUserStoreManager();

            // Checks if the user already exists on the IdP User Store.
            boolean userExist = userStoreMgr.isExistingUser(subject);

            if (userExist) {
                String userEmail = userStoreMgr.getUserClaimValue(subject, UserCoreConstants.ClaimTypeURIs.EMAIL_ADDRESS, null);
                if (StringUtils.isEmpty(userEmail)) {
                    userEmail = subject.toLowerCase(Locale.ROOT) + EMAIL_DOMAIN;
                    attributes.putIfAbsent(UserCoreConstants.ClaimTypeURIs.EMAIL_ADDRESS, userEmail);
                }
            }
            else {
                if (CollectionUtils.isEmpty(roles)) {
                    roles = new ArrayList<>(0);
                    roles.add(DEFAULT_USER_ROLE_NAME);
                }
                else {
                    if (roles.stream().noneMatch(role -> role.equalsIgnoreCase(DEFAULT_USER_ROLE_NAME)))
                        roles.add(DEFAULT_USER_ROLE_NAME);
                }
            }

            // Passes on the manipulated roles and attributes objects
            // for the JIT superclass handler to process.
            super.handle(roles, subject, attributes, provisioningUserStoreId, tenantDomain);

        } catch (Exception e) {
            log.error("Internal error in Custom JIT Provisioning handler", e);
        }
    }
}