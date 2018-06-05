import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by srikanth.kantamaneni on 09/06/17.
 */
public class BasicAuthHttpClient {
    protected final String host;
    protected final String userName;
    protected final String password;

    public BasicAuthHttpClient(String host, String userName, String password) {
        this.host = host;
        this.userName = userName;
        this.password = password;
    }

    public HttpHost getTargetHost() throws MalformedURLException {
        URL lmsApiUrl = new URL(host);

        String lmsApiHostname = lmsApiUrl.getHost();
        int port = lmsApiUrl.getPort() > 0 ? lmsApiUrl.getPort() : 80;
        String scheme = lmsApiUrl.getProtocol();

        HttpHost targetHost = new HttpHost(lmsApiHostname, port, scheme);
        return targetHost;
    }

    public HttpClientContext getHttpClientContext() throws MalformedURLException {
        HttpHost targetHost = this.getTargetHost();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials(userName, password)
        );

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        return context;
    }
}

