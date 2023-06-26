
<div align="center">
  
<h1 align="center">azure-openai-proxy</h1>

中文 | [English](./README.md)

🚀 一个代理OpenAI API请求到Azure OpenAI Service的应用程序，支持流输出和打字效果.

</div>

# 支持的项目
| 名称                                                       | 状态  |
|----------------------------------------------------------|-----|
| [chatgpt-web](https://github.com/Chanzhaoyu/chatgpt-web) | ✅   |

## 在线体验
-> [https://chat.kongpf8848.com](https://chat.kongpf8848.com)

![](https://github.com/kongpf8848/azure-openai-proxy/blob/master/assets/chatgpt-web.webp) 

## 开始

### 获取密钥和终结点

要成功调用Azure OpenAI Service，需要提供以下内容:

| 名称                         | 描述                                                                                                                                                                                                                                                                                 | Default                                                  |
|----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| ----------------------------- |
| AZURE_OPENAI_ENDPOINT      | This value can be found in the **Keys & Endpoint** section when examining your resource from the Azure portal. Alternatively, you can find the value in **Azure OpenAI Studio** > **Playground** > **Code View**. An example endpoint is: `https://test.openai.azure.com/`         | https://xxx.openai.azure.com/ |
| AZURE_OPENAI_DEPLOYMENT_ID | This value will correspond to the custom name you chose for your deployment when you deployed a model. This value can be found under **Resource Management** > **Deployments** in the Azure portal or alternatively under **Management** > **Deployments** in Azure OpenAI Studio. | xxx |
| AZURE_OPENAI_API_VERSION   | [See here](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/quickstart?tabs=command-line&pivots=rest-api) or Azure OpenAI Studio                                                                                                                                  | 2023-03-15-preview |

### 构建

````shell

./gradlew build

docker build -t azure-openai-proxy .
````

### 使用Docker

````shell
docker run -d -p 8080:8080 \
  --env AZURE_OPENAI_ENDPOINT=your_azure_endpoint \
  --env AZURE_OPENAI_DEPLOYMENT_ID=your_azure_deployment_id \
  --env AZURE_OPENAI_API_VERSION=your_azure_api_version \
  rainboy2010/azure-openai-proxy:latest
````

### 使用Curl

````shell
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
````

### 使用ChatGPT-Web

ChatGPT Web: https://github.com/Chanzhaoyu/chatgpt-web


环境变量:

- `OPENAI_API_KEY` Azure OpenAI API Key
- `AZURE_OPENAI_ENDPOINT` Azure OpenAI API Endpoint
- `AZURE_OPENAI_DEPLOYMENT_ID` Azure OpenAI API Deployment Id
- `AZURE_OPENAI_API_VERSION` option，default is 2023-03-15-preview
  
docker-compose.yml:

````yaml
version: '3'

services:
  chatgpt-web:
    image: chenzhaoyu94/chatgpt-web
    ports:
      - 3002:3002
    environment:
      OPENAI_API_KEY: <Azure OpenAI API Key>
      OPENAI_API_BASE_URL: http://azure-openai:8080
      # OPENAI_API_MODEL: gpt-4
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
    networks:
      - chatgpt-ns

networks:
  chatgpt-ns:
    driver: bridge
````

运行:

````shell
docker compose up -d
````


