package util;

import stream.OutputColor;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    private OutputColor type;
    private String response;
    Result res;

    public OutputColor getType() {
        return type;
    }

    public String getResponse() {
        return response;
    }

    public Result getRes() {
        return res;
    }

    public ServerResponse(String response, OutputColor type, Result res){
        this.response=response;
        this.type=type;
        this.res=res;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "type=" + type +
                ", response='" + response + '\'' +
                '}';
    }
}
