Vagrant.configure("2") do |config|
  config.vm.box = "generic/alpine39"

  config.vm.provider "virtualbox" do |v|
    v.name = "~{projectName}_dev"
  end

  config.vm.network "forwarded_port", guest: 5432, host: 5432

  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "dev_playbook.yml"
  end
end
