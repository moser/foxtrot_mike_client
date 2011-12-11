IO.popen('sbt "~test"') do |f|
  while(l = f.gets) do
    if l =~ /Passed: /
      `notify-send 'FMC: Tests passed' -i ~/code/foxtrot_mike/.notify-img/passed.png &> /dev/null`
    elsif l =~ /Compilation failed/
      `notify-send 'FMC: Compilation failed' -i ~/code/foxtrot_mike/.notify-img/error.png &> /dev/null`
    elsif l =~ /Failed: /
      `notify-send 'FMC: Test(s) failed' -i ~/code/foxtrot_mike/.notify-img/failed.png &> /dev/null`
    end
    puts l
  end
end

