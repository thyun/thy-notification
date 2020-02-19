# thy-notification
- Provides centralized notification service for alarm or alert message.
- Provides UI to manage target addresses such as phone numbers (mobile), email addresses and slack webhook address, etc.
- Supports prometheus alertmanager's webhook request.

# How to run
- Download thy-notification.tar.gz file from https://github.com/thyun/thy-notification/releases
- Unzip the file, change to the home directory and run the following
  - java -jar thy-notification.jar
- Access http://localhost:8080/ in your browser

# Config files
- config/application.yml

# Usage
### 1) Edit config/application.yml
Modify application.alertmanager configuration. The following example shows targetId is set as team name. 
So you can send messages to target addresses by team name. 
```
  alertmanager:
    targetId: team:{{ .commonLabels.team }}
```
Modify application.email configuration.  
Modify application.webhook configuration. The following example generates an outgoing webhook for each request.
```
  webhook:
    - name: sms
      url: http://localhost:9000/send?phoneNum={{ .phone }}&msg={{ .message }}&callbackNum=01099999999
      method: GET
      body: ""
      format: delimiter
```

### 2) Register target addresses for your targetId
- Access thy-notifications's targets page. (/targets)
- Register target addresses for each team.
For example, if team name is devops you have to register a target using targetId value as team:devops

# Test
You can send prometheus alertmanager's webhook request to thy-notification like below.
Verify the message is send to all the target addresses.

```aidl
curl -X POST http://localhost:8080/v1/webhook -H 'Content-Type: application/json' -d '
{
    "receiver": "webhook-alert", 
    "status": "firing", 
    "alerts": [
        {
            "status": "firing", 
            "labels": {
                "alertname": "High Memory Usage of Container", 
                "container_name": "broker", 
                "namespace": "default", 
                "pod_name": "kafka-0", 
                "team": "dev"
            }, 
            "annotations": {
                "summary": "Container named  in  in default is using more than 75% of Memory Limit"
            }, 
            "startsAt": "2020-01-21T09:25:10.878939472Z", 
            "endsAt": "0001-01-01T00:00:00Z", 
            "generatorURL": "http://prometheus-deployment-5f6fb569f5-pft2j:9090/graph?g0.expr=%28%28%28sum+by%28namespace%2C+container_name%2C+pod_name%29+%28container_memory_usage_bytes%7Bcontainer_name%21%3D%22POD%22%2Cimage%21%3D%22%22%2Cnamespace%21%3D%22kube-system%22%7D%29+%2F+sum+by%28namespace%2C+container_name%2C+pod_name%29+%28container_spec_memory_limit_bytes%7Bcontainer_name%21%3D%22POD%22%2Cimage%21%3D%22%22%2Cnamespace%21%3D%22kube-system%22%7D%29%29+%2A+100%29+%3C+%2BInf%29+%3E+75&g0.tab=1"
        }
    ], 
    "groupLabels": {
        "alertname": "High Memory Usage of Container"
    }, 
    "commonLabels": {
        "alertname": "High Memory Usage of Container", 
        "namespace": "default", 
        "team": "devops"
    }, 
    "commonAnnotations": {
        "summary": "Container named  in  in default is using more than 75% of Memory Limit"
    }, 
    "externalURL": "http://prometheus.example.com", 
    "version": "4", 
    "groupKey": "{}:{alertname=\"High Memory Usage of Container\"}"
}
'

```
# Alertmanager's config
Configure prometheus alertmanager to send webhook request to thy-notificatin.
The following shows alertmanager's config example.
```
  receivers:
   - name: "webhook-alert"
      webhook_configs:
      - url: "http://thy-target:8080"
```
