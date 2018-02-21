package org.wso2.carbon.identity.application.mgt;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.crypto.api.CertificateInfo;
import org.wso2.carbon.crypto.api.CryptoContext;
import org.wso2.carbon.crypto.api.KeyResolver;
import org.wso2.carbon.crypto.api.PrivateKeyInfo;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class ServiceProviderKeyResolver extends KeyResolver {

    @Override
    public boolean isApplicable(CryptoContext cryptoContext) {

        return "SERVICE-PROVIDER".equals(cryptoContext.getType());
    }

    @Override
    public PrivateKeyInfo getPrivateKeyInfo(CryptoContext cryptoContext) {

        return null;
    }

    @Override
    public CertificateInfo getCertificateInfo(CryptoContext cryptoContext) {

        try {

            ApplicationManagementService applicationManagementService = ApplicationManagementServiceImpl.getInstance();

            ServiceProvider serviceProvider;
            if(cryptoContext.getIdentifier() != null){
                serviceProvider = applicationManagementService.getServiceProvider(cryptoContext.getIdentifier(), cryptoContext.getTenantDomain());
            }else {

                String clientType = cryptoContext.getProperty("clientType");
                String clientID = cryptoContext.getProperty("clientID");

                serviceProvider = applicationManagementService.getServiceProviderByClientId(clientID, clientType,
                        cryptoContext.getTenantDomain());
            }

            if(serviceProvider != null){

                String certificateContent = serviceProvider.getCertificateContent();
                if (StringUtils.isNotBlank(certificateContent)) {
                    // Build the Certificate object from cert content.
                    Certificate certificate = IdentityUtil.convertPEMEncodedContentToCertificate(certificateContent);

                    return new CertificateInfo(null, certificate);
                }
            }
        } catch (CertificateException e) {
            e.printStackTrace();
        }  catch (IdentityApplicationManagementException e) {
            e.printStackTrace();
        }

        return null;
    }
}
