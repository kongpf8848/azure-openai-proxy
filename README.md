
<div align="center">
  
<h1 align="center">azure-openai-proxy</h1>

中文 | [English](./README_EN.md)

🚀 一个代理OpenAI API请求到Azure OpenAI服务的应用程序，支持流式输出和打字效果。

</div>

## 支持的项目
| 名称                                                       | 状态  |
|----------------------------------------------------------|-----|
| [chatgpt-web](https://github.com/Chanzhaoyu/chatgpt-web) | ✅   |

## 在线体验
🔗 [https://chat.kongpf8848.com](https://chat.kongpf8848.com)

![截图](https://github.com/kongpf8848/azure-openai-proxy/blob/master/assets/chatgpt-web.webp) 

## 开始

### 获取密钥/终结点/部署名称

要成功调用Azure OpenAI服务，需要提供以下内容:

| 名称                         | 描述                                                                                                                                                                                                                                                                                | 默认值                                                  |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| ----------------------------- |
| AZURE_OPENAI_ENDPOINT      | 可以在Azure资源页面 **资源管理** -> **密钥和终结点**部分中找到此值。一个示例端点是:https://test.openai.azure.com/ |https://xxx.openai.azure.com/|
| AZURE_OPENAI_DEPLOYMENT_ID | 此值对应于你在部署模型时选择的自定义名称。这个值可以在Azure OpenAI Studio **管理** -> **部署**下找到。 | xxx |
| AZURE_OPENAI_API_VERSION   |可选，API 版本，遵循 YYYY-MM-DD格式。<br>**支持的版本:**<br>2023-03-15-preview<br>2022-12-01<br>2023-05-15<br>2023-06-01-preview| 2023-03-15-preview |

如下图所示，则

AZURE_OPENAI_ENDPOINT=**https://cctest.openai.azure.com/**

AZURE_OPENAI_DEPLOYMENT_ID=**gpt35**

| 密钥和终结点                         | 部署名称 | 
|----------------------------|----------------------------|
|![azure_01](https://github.com/kongpf8848/azure-openai-proxy/blob/master/assets/azure_cn_01.png) |![azure_02](https://github.com/kongpf8848/azure-openai-proxy/blob/master/assets/azure_cn_02.png) |

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

🔗 https://github.com/Chanzhaoyu/chatgpt-web


环境变量:

- `OPENAI_API_KEY` Azure OpenAI API 密钥
- `AZURE_OPENAI_ENDPOINT` Azure OpenAI API 终结点
- `AZURE_OPENAI_DEPLOYMENT_ID` Azure OpenAI API 部署名称
- `AZURE_OPENAI_API_VERSION` 可选，默认值为2023-03-15-preview
  
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
      OPENAI_API_MODEL: gpt-3.5-turbo
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
## 资源
- [Azure网站-https://portal.azure.com](https://portal.azure.com)
- [Azure OpenAI服务REST API介绍-https://learn.microsoft.com/zh-cn/azure/cognitive-services/openai/reference](https://learn.microsoft.com/zh-cn/azure/cognitive-services/openai/reference)
- [OpenAI API介绍-https://platform.openai.com/docs/api-reference](https://platform.openai.com/docs/api-reference)

