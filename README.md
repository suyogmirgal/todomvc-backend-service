# todomvc-backend-service
This is todomvc backend API service written using java and springboot. This service provides APIs for to manage todo list.
All the unit and integration tests are covered.

This service provides below APIs

1) Create Todo
```$xslt
POST /todo
```

2) Update Todo by todoId
```$xslt
PATCH /todo/{todo-id}
```
3) Get all Todos
```$xslt
GET /todo
```
4) Get Todo by todoId
```$xslt
GET /todo/{todo-id}
```
5) Get all active Todo
```$xslt
GET /todo/active
```
6) Delete todo by todoId
```$xslt
POST /todo/{todo-id}
```
7) Delete/Clear all completed Todo
```$xslt
DELETE /todo/clear-isCompleted-todos
```