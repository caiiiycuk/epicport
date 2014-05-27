use warnings;
use strict;
use diagnostics;

my $pre =<<"__PRE__";
var EpicportApp = function(Module) {
__PRE__

my $post =<<"__POST__";
Module['FS_findObject'] = FS.findObject;
Module['dunCall'] = Runtime.dynCall;
Module['dynCall'] = Runtime.dynCall;
Module.screenIsReadOnly = true;
SDL.defaults.copyOnLock = false;

Module['SDL_numSimultaneouslyQueuedBuffers'] = 1;
};

EpicportApp(Module);
__POST__

my $out = $ARGV[0];
my $in = $out . ".orig";

system("cp $out $in");

open IN, '<:utf8', $in;
open OUT, '>:utf8', $out;

print OUT $pre;

while (<IN>) {
  print OUT $_;
}

print OUT $post;

close IN;
close OUT;

system("rm $in");

print "js-transform: done\n";

