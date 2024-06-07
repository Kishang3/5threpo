(config

(password-field
:name "Api Key"
:label "Api Key"
:placeholder "enter the passowrd hear"

))

(default-source (http/get :base-url "https://api.workos.com"
(header-params "Accept" "application/json"))
(paging/url-key : scroll-key-query-param-name "before",
                  scroll-value-path-in-response  "before", 
                  scroll-key-query-param-name "after",
                  scroll-value-path-in-response  "after", 
                  limit 100, 
                  limit-query-param-name "limit"
(auth/http-barrer)
(error-handler
(when :status 200 :message Successful request)
(when :status 400 :The request was not acceptable)
(when :status 401 :The API key used was invalid)
(when :status 403 :API key used was invalid)
(when :status 404 :The resource was not found)
(when :status 422 :Validation failed for the request)
(when :status 429 Too many requests)
))

(temp-entity organization 
(source (http/get :url "/organization")
(extract-path ""))
(fields 
id ))