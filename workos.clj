(config

  (password-field
    :name        "Api Key"
    :label       "API Key"
    :placeholder "Enter your Packiyo API Key"))

(default-source (http/get :base-url "https://{subDomain}.packiyo.com/api/v1"
                  (header-params "Accept"  "application/vnd.api+json"))
  (paging/no-pagination)
  (auth/http-bearer)
  (error-handler
    (when :status 429 :action rate-limit)))

(temp-entity PRODUCT
  (api-docs-url "https://www.postman.com/packiyo/workspace/public-api/collection/30217643-54cf218a-bbae-40c5-a359-c84f14562896")
  (source (http/get :url "/products")
    (extract-path "data")
    (setup-test
      (upon-receiving :code 200 :action (pass))))
  (fields
    id    :id))
 

(entity DATASET
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/datasets")
  (source (http/get :url "/datasets")
    (setup-test
      (upon-receiving :code 200 :action (pass)))
    (extract-path "entries"))
  (fields
    id  :id       :<=     "datasetId"
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt"
    name
    description
    url))

(entity CONNECTION
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/connections")
  (source (http/get :url "/connections")
    (extract-path "entries"))
  (fields
    organization_id    :<=     "organizationId"
    id      :id        :<=     "connectionId"
    is_sample         :<=     "isSample"
    is_audit_log    :<=     "isAuditLog"
    last_active_at      :<=     "lastActiveAt"
    name
    type
    use_oauth         :<=     "useOauth"
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt"
    account
    warehouse
    users                  :<=          "user"
    host
    role
    friendly_name     :<=     "friendlyName"
    is_archived     :<=     "isArchived")
  (dynamic-fields
    (flatten-fields
      (fields
        defaults :<= "default") :from "timeout")
    (flatten-fields
      (fields
        adhoc_pool_size     :<=     "adhocPoolSize"
        schedule_pool_size   :<=     "schedulePoolSize"
        catalog_pool_size     :<=     "catalogPoolSize"
        result_pool_size    :<=     "resultPoolSize") :from "poolSizes" :prefix "pool_sizes_")))

(entity CONNECTION_GRANT
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/connections/paths/-connectionPathId-/grants")
  (source (http/get :url "/connections/{CONNECTION.id}/grants")
    (extract-path "entries"))
  (fields
    grant_id  :id  :<=             "grantId"
    inode_id
    organization_id :<=    "organizationId"
    member_id       :<=             "memberId"
    team_id         :<=             "teamId"
    permission
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt")
  (relate
    (needs CONNECTION :prop "id")
    (links-to MEMBER :prop "member_id")
    (links-to TEAM :prop "team_id")))

(entity DATASET_GRANT
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/datasets/-datasetId-/grants")
  (source (http/get :url "/datasets/{DATASET.id}/grants")
    (extract-path "entries"))
  (fields
    grant_id  :id    :<=     "grantId"
    inode_id     :<=     "inodeId"
    organization_id    :<=     "organizationId"
    member_id      :<=     "memberId"
    team_id      :<=     "teamId"
    permission
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt")
  (relate
    (needs DATASET :prop "id")
    (links-to MEMBER :prop "member_id")
    (links-to TEAM :prop "team_id")))

(entity DATASET_MATERIALIZATION
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/datasets/-datasetId-/materialization")
  (source (http/get :url "/datasets/{DATASET.id}/materialization")
    (extract-path "entries"))
  (fields
    error
    finished_at   :id  :<=     "finishedAt"
    runtime_secs    :<=     "runtimeSecs"
    num_bytes      :<=     "numBytes"
    num_rows     :<=     "numRows"
    status)
  (relate
    (needs DATASET :prop "id")))

(entity FILE
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/files")
  (source (http/get :url "/files")
    (extract-path "entries"))
  (fields
    id   :id
    url_id           :<=     "urlId"
    name
    type
    parent_id               :<=     "parentId"
    parent_url_id        :<=     "parentUrlId"
    permission
    path
    badge
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt"
    is_archived     :<=     "isArchived"))

(entity GRANT_MEMBER
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/grants")
  (source (http/get :url "/grants"
            (query-params  "userId" "{MEMBER.id}"))
    (extract-path "entries"))
  (fields
    inode_type    :<=     "inodeType"
    grant_id  :id    :<=     "grantId"
    inode_id     :<=     "inodeId"
    organization_id    :<=     "organizationId"
    member_id      :<=     "memberId"
    team_id      :<=     "teamId"
    permission
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt")
  (relate
    (links-to MEMBER :prop "member_id")
    (links-to TEAM :prop "team_id")))

(entity GRANT_TEAM
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/grants")
  (source (http/get :url "/grants"
            (query-params "teamId" "{TEAM.id}"))
    (extract-path "entries"))
  (fields
    inode_type    :<=     "inodeType"
    grant_id  :id          :<=     "grantId"
    inode_id           :<=     "inodeId"
    organization_id    :<=     "organizationId"
    member_id            :<=     "memberId"
    team_id     :id       :<=    "teamId"
    permission
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt")
  (relate
    (links-to TEAM :prop "team_id")
    (links-to MEMBER :prop "member_id")))

(entity MEMBER
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/members")
  (source (http/get :url "/members")
    (extract-path "entries"))
  (fields
    organization_id    :<=     "organizationId"
    id        :id     :<=     "memberId"
    member_type     :<=     "memberType"
    first_name    :<=     "firstName"
    last_name      :<=     "lastName"
    email
    profile_img_url    :<=     "profileImgUrl"
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt"
    home_folder_id      :<=     "homeFolderId"
    user_kind         :<=     "userKind"))

(entity MEMBER_FILE
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/members/-memberId-/files")
  (source (http/get :url "/members/{MEMBER.id}/files")
    (extract-path "entries"))
  (fields
    id      :id      ; file_id
    url_id     :<=     "urlId"
    name
    type
    parent_id      :<=     "parentId"
    parent_url_id     :<=     "parentUrlId"
    permission
    path
    badge
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt"
    is_archived    :<=     "isArchived")
  (relate
    (needs MEMBER :prop "id")
    (links-to FILE :prop "id")))

(entity TAGS
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/tags")
  (source (http/get :url "/tags")
    (extract-path "entries"))
  (fields
    id        :id   :<=     "versionTagId"
    name
    owner_id     :<=     "ownerId"
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt"))

(entity TEAM
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/teams")
  (source (http/get :url "/teams")
    (extract-path "entries"))
  (fields
    id        :id   :<=     "teamId"
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt"
    name
    description
    visibility))

(entity USER_ATTRIBUTE
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/user-attributes")
  (source (http/get :url "/user-attributes")
    (extract-path "entries"))
  (fields
    id   :id      :<=     "userAttributeId"
    name
    description
    default_value   :<=     "defaultValue"
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt")
  (dynamic-fields
    (flatten-fields
      (fields
        type
        val) :from "defaultValue")))

(entity WORKBOOK
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/workbooks")
  (source (http/get :url "/workbooks")
    (extract-path "entries"))
  (fields
    id  :id     :<=     "workbookId"
    workbook_url_id    :<=     "workbookUrlId"
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt"
    name
    url
    path
    latest_version   :<=     "latestVersion"))

(entity WORKBOOK_QUERY
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/workbooks/-workbookId-/queries")
  (source (http/get :url "/workbooks/{WORKBOOK.id}/queries")
    (extract-path "entries"))
  (fields
    id  :id  :<=     "elementId"
    name
    sql)
  (relate
    (needs WORKBOOK :prop "id")))

(entity WORKBOOK_MATERIALIZATION_SCHEDULE
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/workbooks/-workbookId-/materialization-schedules")
  (source (http/get :url "/workbooks/{WORKBOOK.id}/materialization-schedules")
    (extract-path "entries"))
  (fields
    id  :id     :<=     "sheetId"
    element_name    :<=     "elementName"
    configured_at     :<=     "configuredAt")
  (dynamic-fields
    (flatten-fields
      (fields
        cron_spec     :<=     "cronSpec"
        timezone) :from "schedule"))
  (relate
    (needs WORKBOOK :prop "id")))

(entity WORKSPACE
  (api-docs-url "https://docs.sigmacomputing.com/api/v2/#get-/v2/workspaces")
  (source (http/get :url "/workspaces")
    (extract-path "entries"))
  (fields
    id  :id     :<=     "workspaceId"
    name
    created_by    :<=     "createdBy"
    updated_by    :<=     "updatedBy"
    created_at    :<=     "createdAt"
    updated_at    :<=     "updatedAt"))
 