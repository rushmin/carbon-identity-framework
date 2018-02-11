/*
 * Copyright (c) 2015 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.user.mgt.listeners;

import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.core.AbstractIdentityUserOperationEventListener;
import org.wso2.carbon.user.api.Permission;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.util.UserOperationsAuditLogger;

import java.util.Arrays;
import java.util.Map;

import static org.wso2.carbon.user.core.util.UserOperationsAuditLogger.ActionResult.SUCCESS;
import static org.wso2.carbon.user.core.util.UserOperationsAuditLogger.USER_OPERATION_ADD_ROLE;
import static org.wso2.carbon.user.core.util.UserOperationsAuditLogger.USER_OPERATION_ADD_USER;
import static org.wso2.carbon.user.core.util.UserOperationsAuditLogger.USER_OPERATION_CHANGE_PASSWORD_BY_ADMINISTRATOR;
import static org.wso2.carbon.user.core.util.UserOperationsAuditLogger.USER_OPERATION_CHANGE_PASSWORD_BY_USER;
import static org.wso2.carbon.user.core.util.UserOperationsAuditLogger.USER_OPERATION_DELETE_ROLE;
import static org.wso2.carbon.user.core.util.UserOperationsAuditLogger.USER_OPERATION_DELETE_USER;
import static org.wso2.carbon.user.core.util.UserOperationsAuditLogger.USER_OPERATION_UPDATE_ROLE_NAME;
import static org.wso2.carbon.user.core.util.UserOperationsAuditLogger.USER_OPERATION_UPDATE_USERS_OF_ROLE;

public class UserMgtAuditLogger extends AbstractIdentityUserOperationEventListener {

    public boolean doPostAddUser(String userName, Object credential, String[] roleList, Map<String, String> claims,
                                 String profile, UserStoreManager userStoreManager) throws UserStoreException {

        if(!isEnable()) {
            return true;
        }

        StringBuilder builder = new StringBuilder();
        if (roleList != null) {
            for (int i = 0; i < roleList.length; i++) {
                builder.append(roleList[i] + ",");
            }
        }

        UserOperationsAuditLogger.log(USER_OPERATION_ADD_USER, userName, "Roles :" + builder.toString(), SUCCESS);
        return true;
    }

    public boolean doPostDeleteUser(String userName, UserStoreManager userStoreManager) throws UserStoreException {

        if(!isEnable()) {
            return true;
        }

        UserOperationsAuditLogger.log(USER_OPERATION_DELETE_USER, userName, "", SUCCESS);
        return true;
    }

    public boolean doPostUpdateCredential(String userName, Object credential, UserStoreManager userStoreManager) throws
            UserStoreException {

        if(!isEnable()) {
            return true;
        }

        UserOperationsAuditLogger.log(USER_OPERATION_CHANGE_PASSWORD_BY_USER, userName, "", SUCCESS);
        return true;
    }

    public boolean doPreUpdateCredentialByAdmin(String userName, Object newCredential, UserStoreManager
            userStoreManager) throws UserStoreException {

        if(!isEnable()) {
            return true;
        }

        UserOperationsAuditLogger.log(USER_OPERATION_CHANGE_PASSWORD_BY_ADMINISTRATOR, userName, "", SUCCESS);
        return true;
    }

    public boolean doPostDeleteRole(String roleName, UserStoreManager userStoreManager) throws UserStoreException {

        if(!isEnable()) {
            return true;
        }

        UserOperationsAuditLogger.log(USER_OPERATION_DELETE_ROLE, roleName, "", SUCCESS);
        return true;
    }

    public boolean doPostAddRole(String roleName, String[] userList, Permission[] permissions, UserStoreManager
            userStoreManager) throws UserStoreException {

        if(!isEnable()) {
            return true;
        }

        UserOperationsAuditLogger.log(USER_OPERATION_ADD_ROLE, roleName,
                "Users : " + Arrays.toString(userList) + " Permissions : " + Arrays.toString(permissions),
                SUCCESS);
        return true;
    }

    public boolean doPostUpdateRoleName(String roleName, String newRoleName, UserStoreManager userStoreManager)
            throws UserStoreException {

        if(!isEnable()) {
            return true;
        }

        UserOperationsAuditLogger.log(USER_OPERATION_UPDATE_ROLE_NAME, roleName,
                "Old : " + roleName + " New : " + newRoleName, SUCCESS);
        return true;
    }

    public boolean doPostUpdateUserListOfRole(String roleName, String[] deletedUsers, String[] newUsers,
                                              UserStoreManager userStoreManager) throws UserStoreException {

        if(!isEnable()) {
            return true;
        }

        UserOperationsAuditLogger.log(USER_OPERATION_UPDATE_USERS_OF_ROLE, roleName,
                "Users : " + Arrays.toString(newUsers), SUCCESS);
        return true;
    }

    public boolean doPostUpdateRoleListOfUser(String userName, String[] deletedRoles, String[] newRoles,
                                              UserStoreManager userStoreManager) throws UserStoreException {
        // We can't log the event here due to incomplete context data. It is logged by the user core.
        // This change was done as a part of fixing https://github.com/wso2/product-apim/issues/2678
        return true;
    }
}
