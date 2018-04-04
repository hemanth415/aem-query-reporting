package vo;

import execute.ExecuteCurl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hemanthponnuru on 4/3/18
 * CURL properties reference from https://curl.haxx.se/docs/manual.html
 * Current purpose is to create a CURL command is get a JSON file over HTTP/HTTPS with user information.
 * Builder pattern is being used. (https://jlordiales.me/2012/12/13/the-builder-pattern-in-practice/)
 */
public class CreateCURL {

    private String username;

    private String password;

    private String url;

    private List<String> commands;

    public CreateCURL(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.url = builder.url;
        createCommands();
    }

    private void createCommands(){
        commands = new ArrayList<>();
        commands.add(ExecuteCurl.CURL);
        commands.add(ExecuteCurl.USER);
        commands.add(username + ":"+ password);
        commands.add(url);
    }


    public List<String> getCommands() {
        return commands;
    }

    public static class Builder {

        private String username;

        private String password;

        private String url;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public CreateCURL build() {
            return new CreateCURL(this);
        }
    }
}
