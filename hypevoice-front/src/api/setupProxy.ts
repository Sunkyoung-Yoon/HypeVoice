import {
    createProxyMiddleware,
    //   RequestHandler,
    //   Filter,
    //   Options,
    //   Request,
  } from "http-proxy-middleware";
  
  const setupProxy = (app: any) => {
    app.use(
      "/api",
      createProxyMiddleware({
        target: "https://localhost:8080",
        pathRewrite: {
          "^/api": "/api",
        },
        changeOrigin: true,
      })
    );
  };
  
  export default setupProxy;