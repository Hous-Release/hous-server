package hous.api.external.client.apple;

public interface AppleTokenProvider {

    String getSocialIdFromIdToken(String idToken);
}
