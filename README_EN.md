<div align="center">
  
<h1 align="center">azure-openai-proxy</h1>

[![license](https://img.shields.io/github/license/modelscope/modelscope.svg)](https://github.com/kongpf8848/azure-openai-proxy/blob/master/LICENSE)

English | [中文](./README.md)

🚀 An application that proxy OpenAI API requests to the Azure OpenAI service，support streaming output and typing effects.

![proxy](https://github.com/kongpf8848/azure-openai-proxy/blob/master/assets/proxy.png) 
</div>

## Support projects
| Name                                                     | Status |
| -------------------------------------------------------- | ------ |
| [chatgpt-web](https://github.com/Chanzhaoyu/chatgpt-web) | ✅   |

## Online experience
🔗 [https://chat.kongpf8848.com](https://chat.kongpf8848.com)

![screenshot](https://github.com/kongpf8848/azure-openai-proxy/blob/master/assets/chatgpt-web.webp) 

## Get Start

### Retrieve api key / endpoint / deployment id

| Name                  | Desc                                                                                                                                                                                          | Default                                                  |
| --------------------- |-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| ----------------------------- |
| OPENAI_API_KEY | This value can be found in the **Keys & Endpoint** section when examining your resource from the Azure portal.An example value is: `3e5d77a442fe4ea7b356c66ad412358d`                        |  |
| AZURE_OPENAI_ENDPOINT | This value can be found in the **Keys & Endpoint** section when examining your resource from the Azure portal.An example endpoint is: `https://test.openai.azure.com/`                        | https://xxx.openai.azure.com/ |
| AZURE_OPENAI_DEPLOYMENT_ID   | This value will correspond to the custom name you chose for your deployment when you deployed a model. This value can be found under **Management** > **Deployments** in Azure OpenAI Studio. | xxx |
| AZURE_OPENAI_API_VERSION  | Optional，API version，follow the YYYY-MM-DD format.<br>**Supported versions:**<br>2023-03-15-preview<br>2022-12-01<br>2023-05-15<br>2023-06-01-preview                                                  | 2023-03-15-preview |

### Build

```shell
./gradlew build

//build single platform
docker build -t azure-openai-proxy .

//build multiple platform
docker buildx build -t azure-openai-proxy --platform linux/amd64,linux/arm64 . --push

```

### Use Docker

```shell
docker run -d -p 8080:8080 \
  --env AZURE_OPENAI_ENDPOINT=your_azure_endpoint \
  --env AZURE_OPENAI_DEPLOYMENT_ID=your_azure_deployment_id \
  --env AZURE_OPENAI_API_VERSION=your_azure_api_version \
  rainboy2010/azure-openai-proxy:latest
```

### Use Curl

```shell
curl --location --request POST 'localhost:8080/v1/chat/completions' \
-H 'Authorization: Bearer <Azure OpenAI Key>' \
-H 'Content-Type: application/json' \
-d '{
    "max_tokens": 1000,
    "model": "gpt-3.5-turbo",
    "temperature": 0.8,
    "top_p": 1,
    "presence_penalty": 1,
    "messages": [
        {
            "role": "user",
            "content": "hello"
        }
    ],
    "stream": true
}'
```

### Use ChatGPT-Web

🔗 [https://github.com/Chanzhaoyu/chatgpt-web](https://github.com/Chanzhaoyu/chatgpt-web)

Environments:

- `OPENAI_API_KEY` Azure OpenAI API Key
- `AZURE_OPENAI_ENDPOINT` Azure OpenAI API Endpoint
- `AZURE_OPENAI_DEPLOYMENT_ID` Azure OpenAI API Deployment Id
- `AZURE_OPENAI_API_VERSION` option，API version，default is 2023-03-15-preview
- `AZURE_OPENAI_API_DELAY` option，Stream output with delay interval every time，the unit is milliseconds，default is 50
- 
docker-compose.yml:

```yaml
version: '3'

services:
  chatgpt-web:
    image: chenzhaoyu94/chatgpt-web
    ports:
      - 3002:3002
    environment:
      OPENAI_API_KEY: <Azure OpenAI API Key>
      OPENAI_API_BASE_URL: http://azure-openai:8080
      OPENAI_API_MODEL: gpt-3.5-turbo
      #OPENAI_API_MODEL: gpt-3.5-turbo-16k
      MAX_REQUEST_PER_HOUR: 1000
      TIMEOUT_MS: 600000
    depends_on:
      - azure-openai
    links:
      - azure-openai
    networks:
      - chatgpt-ns

  azure-openai:
    image: rainboy2010/azure-openai-proxy
    ports:
      - 8080:8080
    environment:
      AZURE_OPENAI_ENDPOINT: <Azure OpenAI API Endpoint>
      AZURE_OPENAI_DEPLOYMENT_ID: <Azure OpenAI API Deployment ID>
      AZURE_OPENAI_API_VERSION: 2023-03-15-preview
      #AZURE_OPENAI_API_VERSION: "2023-05-15"
    networks:
      - chatgpt-ns

networks:
  chatgpt-ns:
    driver: bridge
```
Replace `<Azure OpenAI API Key>`，`<Azure OpenAI API Endpoint>`，`<Azure OpenAI API Deployment ID>` with the specific value

Run:

```shell
docker compose up -d
```

## Resources
- [Azure Website-https://portal.azure.com](https://portal.azure.com)
- [Azure OpenAI Service REST API reference-https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference)
- [OpenAI API reference-https://platform.openai.com/docs/api-reference](https://platform.openai.com/docs/api-reference)

