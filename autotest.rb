IO.popen('sbt "~test"') do |f|
  while(l = f.gets) do
    if l =~ /All tests PASSED/
      `notify-send 'Tests passed' -i ~/traffic-light-green.jpg &> /dev/null`
    elsif l =~ /Compilation failed/
      `notify-send 'Compilation problems' -i ~/traffic-light-red.jpg &> /dev/null`
    elsif l =~ /Error running test/
      `notify-send 'Test(s) failed' -i ~/traffic-light-red.jpg &> /dev/null`
    end
    puts l
  end
end

