"use strict";

function nary(f, nargs) {
    let retval = (...args) => (...vars) => f(...args.map(arg => arg(...vars)));
    // :NOTE: It's JavaScript, baby!
    retval.nargs = nargs || f.length;
    return retval;
}

let variableNames = { 'x': 0, 'y': 1, 'z': 2 };

let add = nary((l, r) => l + r);
let subtract = nary((l, r) => l - r);
let multiply = nary((l, r) => l * r);
let divide = nary((l, r) => l / r);
let negate = nary(x => -x);
let avg5 = nary((...args) => args.reduce((x, y) => x + y) / args.length, 5);
avg5.nargs = 5;
let med3 = nary((...args) => args.sort((x, y) => x - y)[Math.floor(args.length / 2)], 5);
med3.nargs = 3;

let variable = name => {
    const i = variableNames[name];
    return (...vars) => vars[i];
};
let cnst = val => () => val;
let pi = cnst(Math.PI);
let e = cnst(Math.E);

let constNames = { 'pi': pi, 'e': e };
let functionNames = {
    '+': add,
    '-': subtract,
    'negate': negate,
    '*': multiply,
    '/': divide,
    'avg5': avg5,
    'med3': med3
};

let parse = expression => expression.trim().split(/\s+/).reduce(parseToken, []).pop();
function parseToken(stack, token) {
    if (token in functionNames) {
        stack.push(functionNames[token](...stack.splice(-functionNames[token].nargs)));
    } else if (token in variableNames) {
        stack.push(variable(token));
    } else if (token in constNames) {
        stack.push(constNames[token])
    } else if (!isNaN(token)) {
        stack.push(cnst(parseInt(token, 10)));
    } else {
        throw "Unexpected token: " + token;
    }
    return stack;
}

let expression = parse("x x * 2 x * - 1 +")
for (let i = 0; i <= 10; i++) {
    println(expression(i));
}