require 'yaml'

def camelcase(s)
  return s if s !~ /_/ && s =~ /[A-Z]+.*/
  res = s.split('_').map {|e| e.capitalize }.join
  res[0].downcase + res[1..-1]
end

def generate(infile, outpath="src/main/scala/models/generated")
  input = YAML.load_file(infile)
  out = STDOUT

  out.puts "package fmclient.models.repos"
  out.puts "import net.liftweb.json._"
  out.puts "import fmclient.models._"
  out.puts "import net.liftweb.json.JsonDSL._"

  out.puts "abstract class Marshaller[T] {"
  out.puts "  def unmarshalJson(j : JValue) : T"
  out.puts "  def update(local : T, remote : T)"
  out.puts "  def marshal(obj : T) : JValue"
  out.puts "}"

  out.puts "object GeneratedJsonConverters {"
  out.puts "  implicit val formats = DefaultFormats"

  input.each do |model, mappings|
    out.puts "  object #{model}Marshaller extends Marshaller[#{model}] {"
    out.puts "    def marshal(o : #{model}) = {"
    out.puts "      ("
    out.puts('        ' + mappings.map { |field, type|
      if type.is_a?(Hash)
        fk = camelcase(field)
        assoc = fk[0..-3]
        "(\"#{field}\" -> o.#{assoc}.id)"
      else
        fieldcc = camelcase(field)
        "(\"#{field}\" -> o.#{fieldcc})"
      end
    }.join(" ~\n        "))
    
    out.puts "      )"
    out.puts "    }"
    
    out.puts "    def update(local : #{model}, remote : #{model}) {"
    out.puts "      assert(local.id == remote.id)"
    mappings.each do |field, type|
      if type.is_a?(Hash)
        fk = camelcase(field)
        assoc = fk[0..-3]
        type, repository = type['type'], type['repository']
        out.puts "      local.#{assoc} = remote.#{assoc}"
      else
        fieldcc = camelcase(field)
        out.puts "      local.#{fieldcc} = remote.#{fieldcc}"
      end
    end
    out.puts "    }"

    out.puts "    def unmarshalJson(value: JValue) = {"
    out.puts "      val res = new #{model}()"
    mappings.each do |field, type|
      if type.is_a?(Hash)
        fk = camelcase(field)
        assoc = fk[0..-3]
        type, repository = type['type'], type['repository']
        out.puts "      res.#{assoc} = #{repository}.find((value \\ \"#{field}\").extract[#{type}])"
      else
        fieldcc = camelcase(field)
        out.puts "      res.#{fieldcc} = (value \\ \"#{field}\").extract[#{type}]"
      end
    end
    out.puts "      res"
    out.puts "    }"
    out.puts "  }"
  end

  out.puts "}"
end

generate('json_protocol.yml')
