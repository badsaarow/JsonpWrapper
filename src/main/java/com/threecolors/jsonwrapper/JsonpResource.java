package com.threecolors.jsonwrapper;

import java.io.IOException;

import org.apache.commons.codec.DecoderException;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * Resource which has only one representation.
 * 
 */
public class JsonpResource extends ServerResource {

    @Get
    public String represent() {
        String url = this.getRequest().getAttributes().get("url").toString();
        Form frm =  this.getRequest().getResourceRef().getQueryAsForm();
        String callback = frm.getFirstValue("callback");
         
        if ( callback.isEmpty() | url.isEmpty() ) {
            this.getResponse().setStatus(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
            return "callback or url is empty";
        }
        
        String jsonp = "";
        try {
            url = new String(org.apache.commons.codec.net.URLCodec.decodeUrl(url.getBytes()));
            Client client = new Client(new Context(), Protocol.HTTP);
            ClientResource target = new ClientResource(url);
            target.setNext(client);
            target.get();
            
            if ( target.getStatus().isSuccess() ) {
                String response = target.getResponseEntity().getText();
                System.out.println("response : " + response);
                
                this.getResponse().setStatus(Status.SUCCESS_OK);
                jsonp =  callback + "(" + response + ")";                
            } else {
                System.out.println("error:" + target.getStatus());
                this.getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } catch (DecoderException e) {
            e.printStackTrace();
            this.getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        } catch (ResourceException e) {
            e.printStackTrace();
            this.getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        } catch (IOException e) {
            System.out.println("exception : " + e.toString());
            this.getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
        
        return jsonp;
    }

}
