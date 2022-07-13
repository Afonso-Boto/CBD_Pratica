capicuaFinder = function capicuaFinder() {
    var capicuas = [];

    var cursor = db.phones.find({}, {number: "$components.number"});
    cursor.forEach(element => {
        let numero = element.number.toString();
        if(isCapicua(numero))
            capicuas.push(numero);
        
    });
    return capicuas;
}

isCapicua = function isCapicua(number) {
    for(var i = 0; i < Math.floor(number/2); i++){
        if( number.charAt(i) != number.charAt(number.length - 1 - i)){
            return false;
        }
    }

    return true;
}