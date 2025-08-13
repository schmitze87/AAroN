package aaron.sparx.config;

public class MSSQLDB extends DBToImport{

    private String instance = "";
    private boolean trustCertificate = false;
    private AuthenticationType authenticationType = AuthenticationType.SQL_LOGIN;

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public boolean isTrustCertificate() {
        return trustCertificate;
    }
    public void setTrustCertificate(boolean trustCertificate) {
        this.trustCertificate = trustCertificate;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public enum AuthenticationType {
        SQL_LOGIN,
        AD_LOGIN
    }
}
