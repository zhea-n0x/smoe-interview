function fizzBuzz(limit){
  Array.from({length: limit}, (_,i) => i+1).forEach((num) => {
    const res = (num % 3 === 0 ? "Fizz" : "") + (num % 5 === 0 ? "Buzz" : "");
    console.log(res || num);
  })
}

fizzBuzz(20);