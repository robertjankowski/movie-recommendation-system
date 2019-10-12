# music-recommendation-system

### Run
```bash
docker-compose -f docker-compose.yml up -d
```
App is available on `localhost:8080`

### Troubleshooting
> Bind failed for TCP channel on endpoint [/0.0.0.0:8080] java.net.BindException: Address already in use: bind

For Windows:
1. Get PID 
```bash
netstat -aon | findstr 8080 | awk '{print $5}' | head -1
```
2. Kill process
```bash
taskkill /F /PID the_output_from_above_command
```