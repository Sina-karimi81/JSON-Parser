# JSON parser
Inspired by the [Jackson-databind](https://github.com/FasterXML/jackson-databind) project, this project is JSON serializer and deserializer, aimed to work with Java's standard library specialy reflection.

## Libraries
Libraries used in this project
- Junit 5
- Lombok
- jackson-databind (for testing and output validation)

## Code
You can devide this project to two parts:
1. **Serializer**
2. **Deserializer**
The entry point of this library is the [JsonMapper](src/main/java/JsonMapper.java) class, which you can checkout yourself.

### Serializer
The serialization part was the _easy_ part, I used the [Template](https://refactoring.guru/design-patterns/template-method) and [Strategy](https://refactoring.guru/design-patterns/strategy) design patterns together to create the logic. basically we have three types of data:
1. Collections
2. Objects
3. Primitive Types
toghether these form our strategies for data marshalling and I decide on the type of convertor to use based on the field type. You can check the [TypeUtils](src/main/java/util/TypeUtils.java) to see how I check for a field type. After I got the field type, I got it's value for the input object and passed to the selected convertor class.

I validated my output using the `ObjectMapper` class of the jackson-databind library. you can see the test cases at [JsonMapperMarshallingTest](src/test/java/JsonMapperMarshallingTest.java) class.

### Deserializer
This was the part that got me, and it got me **hard**. Basically, I treated the deaserialization process like a compiler would, I broke my logic to a Lexer, a Parser and a Semantic Analyzer. I took this insipiration from the Amazing book _Write an Interpreter in Go_, if you look closely my code awfully resembles the author of the book's code :)) 

#### Lexer
The [Lexer](src/main/java/lexer/Lexer.java) class did what it's name suggests: it tokenized the input string which I would later use in the Parser. this class is basically a huge switch case statement that checks for the different patterns of string data and converts them to the tokens that I defined

#### Parser
In the [Parser](src/main/java/parser/Parser.java) class, I pass an instance of the lexer and use it to generate a stream of tokens, my parser is always two tokens behind so we can get 1 lookahead to check for different patterns. From these tokens I try to generate a Parse Tree which I stored in a `Map<String, Node>` with the key being the corresponding field's name
and the value being the actual Node that contains the data

#### Semantic Analyzer
Ahh my white whale, This was the part that got to me. you can check [SemanticAnalyzer](src/main/java/semantic/SemanticAnalyzer.java) class for the code. I tried to validate the type that was in the JSON with the actual type that was in the object and after the types matched populate the field with the value provided, And it mostly went well _until_ I decided to deserialize
**collections**. As you, the well educated audience, might know, Java's generics system was designed to be backward compatible, So the actual datatype gets earsed in the runtime which is called [Type Erasure](https://www.baeldung.com/java-type-erasure). So I couldn't access the actual type that was stored in the generic type and no matter how many times I searched and 
how many solutions I tried, I couldn't make it work.

Finally, I got tired and pushed the project to Github. I have created [issues](https://github.com/Sina-karimi81/JSON-Parser/issues) and if you know anything I'd appreciate it if you could leave a comment or even fix it :))
