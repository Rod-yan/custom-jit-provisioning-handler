package org.wso2.carbon.handler.claim.custom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.authentication.framework.config.model.StepConfig;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.exception.FrameworkException;
import org.wso2.carbon.identity.application.authentication.framework.handler.claims.impl.DefaultClaimHandler;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.HashMap;
import java.util.Map;

public class CustomClaimHandler extends DefaultClaimHandler {

    private static final Log log = LogFactory.getLog(CustomClaimHandler.class);
    private static volatile CustomClaimHandler instance;

    public static CustomClaimHandler getInstance() {
        if (instance == null) {
            synchronized (CustomClaimHandler.class) {
                if (instance == null) {
                    instance = new CustomClaimHandler();
                }
            }
        }
        return instance;
    }

    @Override
    public Map<String, String> handleClaimMappings(StepConfig stepConfig,
                                                   AuthenticationContext context, Map<String, String> remoteAttributes,
                                                   boolean isFederatedClaims) throws FrameworkException {
        UserStoreManager userStoreMgr = getUserStoreManager();
        AuthenticatedUser authenticatedUser;
        Map<String, String> claims = null;

        if (stepConfig != null)
            authenticatedUser = stepConfig.getAuthenticatedUser();
        else
            authenticatedUser = context.getSequenceConfig().getAuthenticatedUser();

        try {
            // Obtaining claims
            claims = super.handleClaimMappings(stepConfig, context, remoteAttributes, isFederatedClaims);
            if (claims == null)
                claims = new HashMap<>();

            // Checking if user already exists on the IdP. If it doesn't, then we create it.
            if (authenticatedUser != null) {
                String userName = authenticatedUser.getUserName();
                if (!userStoreMgr.isExistingUser(userName)) {
                    final boolean requirePasswordChange = true;
                    // TODO: Generate credential object appropriately
                    // userStoreMgr.addUser(userName, credential, null, (Map) claims, userName, requirePasswordChange);
                    userStoreMgr.addUser(userName, null, null, claims, userName, requirePasswordChange);
                }
            }
        } catch (UserStoreException e) {
            e.printStackTrace();
        }

        // WIP ->
        return claims;
    }

    private static UserStoreManager getUserStoreManager() {
        RealmService realmService;
        UserStoreManager userStoreManager;
        try {
            PrivilegedCarbonContext ctx = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            realmService = (RealmService) ctx.getOSGiService(RealmService.class, null);
            if (realmService == null) {
                String msg = "Realm service has not initialized.";
                log.error(msg);
                throw new IllegalStateException(msg);
            }
            int tenantId = ctx.getTenantId();
            userStoreManager = realmService.getTenantUserRealm(tenantId).getUserStoreManager();
            realmService.getTenantUserRealm(tenantId).getAuthorizationManager();
        } catch (UserStoreException e) {
            String msg = "Error occurred while retrieving current user store manager";
            log.error(msg, e);
            throw new IllegalStateException(msg);
        }
        return userStoreManager;
    }
}