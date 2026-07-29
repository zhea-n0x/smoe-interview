function isPalindrome(string){
  const s = string.toLowerCase().replace(/[^a-z0-9]/g, '');
  return s === s.split('').reverse().join('');
}

console.log(`isPalindrome True: ${isPalindrome("kasur ini rusak")}`);
console.log(`isPalindrome False: ${isPalindrome("kasur ini bagus")}`);