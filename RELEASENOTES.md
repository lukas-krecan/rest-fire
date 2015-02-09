## 0.3.1
* Method to() does not overwrite query parameters

## 0.3.0
* Generics cleanup. It's no longer extensible, but the code is at least understandable.

## 0.2.1
* Case sensitive header methods added

## 0.2.0
* Response headers wrapped in a special object
* Breaks backward compatibility, but since the verion 0.1.0 was not announced yet, I think it's not a problem.

## 0.1.0
* Better handling of nulls in response validation
* Generics in ResponseValidator made more generic
* Response can be accessed

## 0.0.8
* Fixed setHeaders function

## 0.0.7
* Changed behavior of RequestBuilder.withHeader and withQueryParameter. Now it overwrites previous values instead of adding.
