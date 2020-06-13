export function joinArray(arr1, arr2) {
  [].push.apply(arr1, arr2);
  return arr1;
}

