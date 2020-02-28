import {validatePatternForString} from "./index"

export const validateEmail = function (email) {
  return validatePatternForString(/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/, email);
};

export const validatePhone = function (phone) {
  return validatePatternForString(/^1[34578]\d{9}$/, phone);
};

export const genValidator = function (validatorFunc, errorMsg) {
  const func = (rule, value, callback) => {
    if (!validatorFunc(value)) {
      callback(new Error(errorMsg));
    } else {
      callback();
    }
  };
  return func;
};
