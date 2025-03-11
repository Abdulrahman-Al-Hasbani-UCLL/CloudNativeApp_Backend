**User**
```
{
  "id": "string",
  "username": "string",
  "email": "string",
  "displayName": "string",
  "password": "pa$$word",
  "emailVerified": true,
  "image": "string",
  "roles": [
    "string"
  ],
  "bio": "string",
  "signature": "string",
  "url": "string",
  "extendedData": {}
}
```

**Follow**
```
{
  "id": "string",
  "followerId": "string",
  "followingId": "string",
  "extendedData": {},
  "createdAt": "string",
  "updatedAt": "string"
}
```

**Tag**
```
{
  "id": "string",
  "name": "string",
  "description": "string",
  "color": "string",
  "threads": [
    {
      "id": "string",
      "title": "string",
      "slug": "string",
      "body": "string",
      "locked": true,
      "pinned": true,
      "user": {},
      "tags": [
        {}
      ],
      "createdAt": "string",
      "updatedAt": "string"
    }
  ],
  "extendedData": {}
}
```

**Thread**
```
{
  "id": "string",
  "title": "string",
  "slug": "string",
  "body": "string",
  "locked": true,
  "pinned": true,
  "user": {
    "id": "string",
    "username": "string",
    "avatar": "string"
  },
  "tags": [
    {
      "id": "string",
      "name": "string",
      "description": "string",
      "color": "string",
      "threads": [
        {}
      ],
      "extendedData": {}
    }
  ],
  "createdAt": "string",
  "updatedAt": "string"
}
```

**Like**
```
{
  "id": "string",
  "userId": "string",
  "threadId": "string",
  "postId": "string",
  "dislike": true,
  "extendedData": {},
  "createdAt": "string",
  "updatedAt": "string"
}
```

**Upvote**
```
{
  "id": "string",
  "userId": "string",
  "threadId": "string",
  "postId": "string",
  "downvote": true,
  "extendedData": {},
  "createdAt": "string",
  "updatedAt": "string"
}
```

**Poll**
```
{
  "id": "string",
  "title": "string",
  "expiresAt": "2019-08-24T14:15:22Z",
  "closed": true,
  "closedAt": "2019-08-24T14:15:22Z",
  "options": [
    {
      "id": "string",
      "title": "string",
      "color": "string",
      "extendedData": {}
    }
  ],
  "extendedData": {},
  "createdAt": "string",
  "updatedAt": "string"
}
```

**Post**
```
{
  "id": "string",
  "body": "string",
  "userId": "string",
  "threadId": "string",
  "parentId": "string",
  "bestAnswer": true,
  "likes": [
    {
      "id": "string",
      "userId": "string"
    }
  ],
  "upvotes": [
    {
      "id": "string",
      "userId": "string"
    }
  ],
  "extendedData": {},
  "instanceId": "string",
  "createdAt": "string",
  "updatedAt": "string"
}
```

**Report**
```
{
  "id": "string",
  "reportedId": "string",
  "reporterId": "string",
  "threadId": "string",
  "postId": "string",
  "privateMessageId": "string",
  "read": true,
  "type": "string",
  "description": "string",
  "instanceId": "string",
  "extendedData": {}
}
```

**ThreadSubscription**
```
{
  "threadId": "string",
  "userId": "string",
  "instanceId": "string",
  "extendedData": {}
}
```

**PollOption**
```
{
  "id": "string",
  "name": "string",
  "votes": 0
}
```

**Role**
```
{
  "id": "string",
  "name": "string",
  "description": "string",
  "color": "string",
  "extendedData": {},
  "createdAt": "string",
  "updatedAt": "string"
}
```
**Notification**
```
{
  "id": "string",
  "userId": "string",
  "type": "string",
  "read": true,
  "extendedData": {},
  "createdAt": "string",
  "updatedAt": "string"
}
```
**PrivateMessage**
```
{
  "id": "string",
  "title": "string",
  "body": "string",
  "senderId": "string",
  "recipientId": "string",
  "read": true,
  "extendedData": {},
  "createdAt": "string",
  "updatedAt": "string"
}
```
**Error**
```
{
  "error": "string",
  "message": "string"
}
```