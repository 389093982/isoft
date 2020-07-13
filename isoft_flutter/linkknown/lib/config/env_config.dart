
class LinkKnownConfig {

  static EnvConfig config;

}

class EnvConfig {
  String apiBaseUrl;
  String hostApiBaseUrl;

  EnvConfig({this.apiBaseUrl, this.hostApiBaseUrl});
}