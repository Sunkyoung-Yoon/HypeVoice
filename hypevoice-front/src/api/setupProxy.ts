import { SERVER_URL } from "@/recoil/SERVER_URL";
import {
    createProxyMiddleware,
    //   RequestHandler,
    //   Filter,
    //   Options,
    //   Request,
  } from "http-proxy-middleware";
import { useRecoilValue } from "recoil";
  
  const setupProxy = (app: any) => {
    app.use(
      "/api",
      createProxyMiddleware({
        target: useRecoilValueZZ(SERVER_URL),
        pathRewrite: {
          "^/api": "/api",
        },
        changeOrigin: true,
      })
    );
  };
  
  export default setupProxy;