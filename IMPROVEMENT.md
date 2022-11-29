
## Backend

### Account Registration

#### Sending Activation Email

> Normally it would do just fine, like the steps of *send activation email*, *click on it* and *enable the account*.
>
> But, once the *email was failed to deliver* because of various reasons like network connection (of the sender), or because of the nature of the `@Async` method. It would produce something like this:
>>
>> - email failed to send
>> - user was still created
>> - token was still attached
>> - THEN there were two (almost) identical user records for *token* to match
>
> Idea
>>
>> - of course, the user record still needs to be saved
>> - let the system to *re-sent the email* (put into a queue or something)
>> - clearer token matching (<> user) even if there were duplicate user records

## Frontend
