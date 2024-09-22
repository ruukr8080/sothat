
## `th:include` and `th:replace` 차이 

Referencing fragments by domselector instead of by `th:fragment`

Thymeleaf provides a "this" option for finding selectors


### fragments/layout.html:

```html
<head th:fragment="headerFragment">
    <title th:include=":: #pageTitle">Layout Generic Title< /title>
    <!-- metas, link and scripts -->
</head>
```

### page.html
```html
<head th:include="fragments/layout :: headerFragment">
    <title id="pageTitle">Page title</title>
    <!-- other elements you want to reference in your layout -->
</head>
```
