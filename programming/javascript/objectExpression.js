'use strict';

let variableNames = ['x', 'y', 'z'];

function Const(value) {
	this.value = value;
}

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.E = new Const(Math.E);

Const.prototype.evaluate = function() {
	return this.value;
};

Const.prototype.toString = function() {
	return this.value.toString();
};

Const.prototype.prefix = Const.prototype.toString;
Const.prototype.postfix = Const.prototype.toString;

Const.prototype.diff = function() {
	return Const.ZERO;
};

function Variable(name) {
	this.index = variableNames.indexOf(name);
	this.name = name;
}

Variable.prototype.evaluate = function(...vals) {
	return vals[this.index];
};

Variable.prototype.toString = function() {
	return this.name;
};

Variable.prototype.diff = function(diffVarName) {
	return this.name === diffVarName ? Const.ONE : Const.ZERO;
};

Variable.prototype.prefix = Variable.prototype.toString;
Variable.prototype.postfix = Variable.prototype.toString;

function Operator(...args) {
	this.args = args;
}

Operator.prototype.evaluate = function(...vals) {
	return this.operation(...this.args.map(arg => arg.evaluate(...vals)));
};

Operator.prototype.toString = function() {
	return [...this.args, this.name].join(' ');
};

Operator.prototype.commonStringRepr = function(isPrefix) {
	if (this.args.length === 0) {
		return '( ' + this.name + ')';
	}
	let body = [...this.args.map(arg => isPrefix ? arg.prefix() : arg.postfix())];
	if (isPrefix) {
		body.unshift(this.name);
	} else {
		body.push(this.name);
	}
	return '(' + body.join(' ') + ')';
};

Operator.prototype.prefix = function() {
	return this.commonStringRepr(true)
};

Operator.prototype.postfix = function() {
	return this.commonStringRepr(false)
};

Operator.prototype.diff = function(diffVarName) {
	return this.diffRule(
		...this.args,
		...this.args.map(arg => arg.diff(diffVarName))
	);
};

let operators = {}; // filled by OperatorFactory

function OperatorFactory(name, f, diff, varArg) {
	let retval = function(...args) {
		return Operator.apply(this, args);
	};
	retval.prototype = Object.create(Operator.prototype);
	retval.prototype.operation = f;
	retval.prototype.diffRule = diff;
	retval.prototype.name = name;
	retval.prototype.nargs = varArg === true ? 0 : f.length;
	operators[name] = retval;
	return retval;
}

let Add = OperatorFactory(
	'+',
	(x, y) => x + y,
	(_, __, df, dg) => new Add(df, dg)
);
let Subtract = OperatorFactory(
	'-',
	(x, y) => x - y,
	(_, __, df, dg) => new Subtract(df, dg)
);
let Negate = OperatorFactory(
	'negate',
	x => -x,
	(_, df) => new Negate(df)
);
let Multiply = OperatorFactory(
	'*',
	(x, y) => x * y,
	(f, g, df, dg) => new Add(new Multiply(f, dg), new Multiply(df, g))
);
let Divide = OperatorFactory(
	'/',
	(x, y) => x / y,
	(f, g, df, dg) =>
		new Divide(
			new Subtract(new Multiply(df, g), new Multiply(f, dg)),
			new Multiply(g, g)
		)
);
let Log = OperatorFactory(
	'log',
	(x, y) => Math.log(Math.abs(y)) / Math.log(Math.abs(x)),
	(f, g, df, dg) =>
		Divide.prototype.diffRule(
			new Log(Const.E, g),
			new Log(Const.E, f),
			new Divide(dg, g),
			new Divide(df, f)
		)
);
let Power = OperatorFactory(
	'pow',
	(x, y) => Math.pow(x, y),
	function(f, g, df, dg) {
		return new Multiply(
			this,
			new Add(
				new Multiply(dg, new Log(Const.E, f)),
				new Multiply(g, new Divide(df, f))
			)
		)
	}
);
let ArcTan = OperatorFactory(
	'atan',
	Math.atan,
	(x, dx) => new Divide(dx, new Add(new Multiply(x, x), Const.ONE))
);
let Exp = OperatorFactory(
	'exp',
	Math.exp,
	function (_, dx) {
		return new Multiply(
			this,
			dx
		)
	}
);
let Sum = OperatorFactory(
	'sum',
	(...vals) => vals.reduce((x, y) => x + y, 0),
	(...valsAndDerivatives) => new Sum(...valsAndDerivatives.splice(valsAndDerivatives.length / 2)),
	true
);
let Avg = OperatorFactory(
	'avg',
	(...vals) => Sum.prototype.operation(...vals) / vals.length,
	(...valsAndDerivatives) => new Divide(Sum.prototype.diffRule(...valsAndDerivatives), new Const(valsAndDerivatives.length / 2)),
	true
);
let Sumexp = OperatorFactory(
	'sumexp',
	(...vals) => vals.map(Math.exp).reduce((x, y) => x + y, 0),
	function(...valsAndDerivatives) {
		let derivatives = valsAndDerivatives.splice(valsAndDerivatives.length / 2);
		let vals = valsAndDerivatives;
		return new Sum(...vals.map((val, i) => new Multiply(new Exp(val), derivatives[i])));
	},
	true
);
let Softmax = OperatorFactory(
	'softmax',
	(x, ...vals) => (Math.exp(x) / Sumexp.prototype.operation(x, ...vals)),
	function(...valsAndDerivatives) {
		// if (valsAndDerivatives.length === 2) {
		// 	return Const.ZERO;
		// }
		let x = valsAndDerivatives[0];
		let dx = valsAndDerivatives[valsAndDerivatives.length / 2];
		let derivatives = valsAndDerivatives.splice(valsAndDerivatives.length / 2);
		let vals = valsAndDerivatives;
		return Divide.prototype.diffRule(
			new Exp(x),
			new Sumexp(...vals),
			new Multiply(dx, new Exp(x)),
			Sumexp.prototype.diffRule(...vals, ...derivatives)
		)
	},
	true
);
let Sinh = OperatorFactory(
	'sinh',
	Math.sinh,
	(x, dx) => new Multiply(dx, new Cosh(x))
);
let Cosh = OperatorFactory(
	'cosh',
	Math.cosh,
	(x, dx) => new Multiply(dx, new Sinh(x))
);

function ParserError(message, reason) {
	this.message = message;
	if (reason !== undefined) {
		this.message += "'" + reason + "'";
	}
}
ParserError.prototype = Object.create(Error.prototype);
ParserError.prototype.name = "ParserError";
ParserError.prototype.constructor = ParserError;

let expectBracket;

function commonParseToken(isPrefix) {
	let openBracket = isPrefix ? ')' : '(';
	let closeBracket = isPrefix ? '(' : ')';
	return function(stack, token) {
		if (expectBracket && token !== closeBracket) {
			throw new ParserError("Expected " + closeBracket + ", not ", token);
		}
		if (token in operators) {
			let operator = operators[token];
			let bracketIdx = stack.lastIndexOf(openBracket);
			if (bracketIdx === -1) {
				throw new ParserError("No opening bracket for ", token);
			}
			let operands = stack.splice(bracketIdx + 1);
			let nargs = operator.prototype.nargs;
			if ((nargs !== 0 && operands.length !== nargs) || operands.some(x => x === closeBracket)) {
				throw new ParserError("Not enough arguments for ", token);
			}
			if (isPrefix) {
				operands.reverse();
			}
			stack.push(new operators[token](...operands));
			expectBracket = true;
			return stack;
		}
		if (variableNames.includes(token)) {
			stack.push(new Variable(token));
		} else if (!isNaN(token)) {
			stack.push(new Const(parseFloat(token)));
		} else if (token === openBracket) {
			stack.push(token);
		} else if (token === closeBracket) {
			if (stack.length < 2) {
				if (stack.pop() === openBracket) {
					throw new ParserError("Empty ", "()");
				} else {
					throw new ParserError("Unmatched ", closeBracket);
				}
			}
			let op = stack.pop();
			if (!(op.name in operators)) {
				throw new ParserError((isPrefix ? "First " : "Last ") + "token in brackets should be an operator, got ", op);
			}
			let shouldBeOpenBracket = stack.pop();
			if (shouldBeOpenBracket !== openBracket) {
				throw new ParserError("Too many arguments, should be ", op.prefix());
			}
			stack.push(op);
		} else {
			throw new ParserError('Unknown token ', token);
		}
		expectBracket = false;
		return stack;
	}
}

let parsePrefixToken = commonParseToken(true);
let parsePostfixToken = commonParseToken(false);

function commonParse(f) {
	return function(expression) {
		expectBracket = false;
		let retval = f(expression
			.replace(/\(/g, ' ( ')
			.replace(/\)/g, ' ) ')
			.trim()
			.split(/\s+/)
			.filter(x => x !== ''));
		if (expectBracket === true) {
			throw new ParserError('No brackets around the expression');
		}
		if (retval.length !== 1) {
			throw new ParserError('No operator in the start');
		}
		return retval.pop(0);
	}
}

let parsePrefix = commonParse(splitExpression => splitExpression.reduceRight(parsePrefixToken, []));
let parsePostfix = commonParse(splitExpression => splitExpression.reduce(parsePostfixToken, []));
