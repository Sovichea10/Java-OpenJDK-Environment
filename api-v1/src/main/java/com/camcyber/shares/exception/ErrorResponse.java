package com.camcyber.shares.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String code;

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private String date;

    private String error ;

    private String path;

    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        json.put("code", this.getCode());
        json.put("date", this.getDate());
        json.put("status", this.getStatus());
        json.put("error", this.getError());
        json.put("path", this.getPath());
        return json;
    }


}
