package serverGui;

import java.io.Serializable;

public class ClientConnInfo implements Serializable {
    private String hostName;
    private String ipAddress;
    private String status;

    public ClientConnInfo(String hostName, String ipAddress, String status) {
        this.hostName = hostName;
        this.ipAddress = ipAddress;
        this.status = status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getHostName() {
        return hostName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getStatus() {
        return status;
    }
}

