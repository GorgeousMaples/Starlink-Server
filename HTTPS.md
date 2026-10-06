# Starlink-Server HTTPS

项目提供可选的 `https` Spring Profile。它与 `dev` 或 `prod` 组合使用，在现有 `8000` 端口启用 TLS。证书与 PKCS#8 PEM 私钥通过 `STARLINK_HTTPS_CERTIFICATE`、`STARLINK_HTTPS_PRIVATE_KEY` 指定，值应为 Spring 资源路径，例如 `file:///D:/certs/server-cert.pem` 或 `file:/etc/starlink/server-cert.pem`。私钥不要提交到仓库。

本地测试可先在 Starlink-Manager 项目运行 `./scripts/create-dev-cert.ps1`。将环境变量指向生成的文件，然后启动后端：

```powershell
$env:STARLINK_HTTPS_CERTIFICATE = 'file:///D:/path/to/Starlink-Manager/certs/localhost-cert.pem'
$env:STARLINK_HTTPS_PRIVATE_KEY = 'file:///D:/path/to/Starlink-Manager/certs/localhost-key.pem'
java -jar target/Starlink-Server-1.0-SNAPSHOT.jar --spring.profiles.active=dev,https
```

验证：`curl.exe -k -I https://localhost:8000/images/card/00004.png`。`-k` 仅用于本地自签证书测试。浏览器和 Electron 使用自签证书前必须将其设为受信任；公网环境需要覆盖实际访问地址的受信任证书。

生产环境使用 `--spring.profiles.active=prod,https`，并设置生产证书路径。启用此 Profile 后，`8000` 端口只接受 HTTPS，原有的 `http://...:8000` 客户端地址需同步改为 `https://...:8000`。如需同时提供 HTTP 与 HTTPS，应在前面部署反向代理并将 HTTP 重定向到 HTTPS。
